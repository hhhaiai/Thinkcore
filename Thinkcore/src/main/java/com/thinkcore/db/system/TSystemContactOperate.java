package com.thinkcore.db.system;

import java.io.InputStream;

import com.thinkcore.TApplication;
import com.thinkcore.db.system.TDBOperate;
import com.thinkcore.utils.TStringUtils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.TelephonyManager;

/*
 * 系统管理通讯录
 * */
public class TSystemContactOperate extends TDBOperate {
	private static String TAG = TSystemContactOperate.class.getCanonicalName();
	private static TSystemContactOperate mThis;

	public TSystemContactOperate() {
	}

	public static TSystemContactOperate getInstance() {
		if (mThis == null) {
			mThis = new TSystemContactOperate();
		}
		return mThis;
	}

	// 系统联系人管理
	public Cursor getSystemCursorByAll() throws Exception { // 获得所有联系人
		return getContentResolver().query(
				Contacts.CONTENT_URI,
				null,
				Contacts.IN_VISIBLE_GROUP + " = '"
						+ (false ? "0" : "1") + "'",
				null,
				Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC"); // 按拼音排序
	}

	public Cursor getSystemCursorLikeNumber(String number) throws Exception { // 获得联系人的电话号码
		if (TStringUtils.isEmpty(number))
			throw new Exception("null by number");
		return getContentResolver().query(
				Phone.CONTENT_URI,
				null,
				Phone.NUMBER + " like '%"
						+ number + "%'", null, null);
	}

	public Cursor getCursorByPhone() throws Exception { // 获得手机联系人
		return getContentResolver().query(
				Phone.CONTENT_URI,
				null,
				null,
				null,
				Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC");
	}

	public Cursor getCursorBySim() throws Exception { // 获得SIM卡联系人
		return getContentResolver().query(
				Uri.parse("content://icc/adn"),
				null,
				null,
				null,
				Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC");
	}

	public Cursor getRawCursorById(String contactId) throws Exception { // 根据contactId查询对应的RawContacts
		if (TStringUtils.isEmpty(contactId))
			throw new Exception("null by contactId");
		return getContentResolver().query(
				ContactsContract.RawContacts.CONTENT_URI, null,
				ContactsContract.RawContacts.CONTACT_ID + " =  ?",
				new String[] { contactId, }, null);
	}

	public Cursor getNumbersCursorByContactId(String contactId)
			throws Exception { // 根据联系人id获得手机
		if (TStringUtils.isEmpty(contactId))
			throw new Exception("null by contactId");
		return getContentResolver().query(
				Phone.CONTENT_URI,
				null,
				Phone.CONTACT_ID + "="
						+ contactId, null, null);
	}

	public Cursor getEmailsCursorByContactId(String contactId) throws Exception { // 根据联系人id获得邮箱
		if (TStringUtils.isEmpty(contactId))
			throw new Exception("null by contactId");
		return getContentResolver().query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI,
				null,
				ContactsContract.CommonDataKinds.Email.CONTACT_ID + "="
						+ contactId, null, null);
	}

	public String getVersion(String contactId) throws Exception {
		if (TStringUtils.isEmpty(contactId))
			throw new Exception("null by contactId");

		Cursor raws = getRawCursorById(contactId);
		String rawVersion = "";

		while (raws.moveToNext()) {
			rawVersion = raws
					.getString(raws
							.getColumnIndexOrThrow(ContactsContract.RawContacts.VERSION));
			if (!TStringUtils.isEmpty(rawVersion))
				break;
		}

		if (raws != null)
			raws.close();
		return rawVersion;
	}

	public boolean hasPhoneContactByNumber(String number) {
		boolean result = false;

		Cursor cursor = null;
		try {
			cursor = getPhoneContactByNumber(number);
			if (cursor != null && cursor.getCount() > 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cursor != null)
			cursor.close();

		return result;
	}

	public Cursor getPhoneContactByNumber(String number) throws Exception {
		if (TStringUtils.isEmpty(number))
			throw new Exception("null by number……");

		return getContentResolver().query(
				Phone.CONTENT_URI,
				null, //
				Phone.NUMBER + " = '" + number
						+ "'", //
				null, // WHERE clause value substitution
				null); // Sort order.
	}

	public String getContactName(String phoneNumber) {
		Cursor cursor = null;
		String contactName = "";
		try {
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phoneNumber));
			cursor = getContentResolver()
					.query(uri, new String[] { PhoneLookup.DISPLAY_NAME },
							null, null, null);
			if (cursor.moveToFirst()) {
				contactName = cursor.getString(cursor
						.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			}
		} catch (Exception e) {
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return contactName;
	}

	public String getDeleted(String contactId) throws Exception {
		if (TStringUtils.isEmpty(contactId))
			throw new Exception("null by contactId");

		Cursor raws = getRawCursorById(contactId);
		String deleted = "";

		while (raws.moveToNext()) {
			deleted = raws
					.getString(raws
							.getColumnIndexOrThrow(ContactsContract.RawContacts.DELETED));
			if (!TStringUtils.isEmpty(deleted))
				break;
		}

		if (raws != null)
			raws.close();
		return deleted;
	}

	public String getPhoneNumber(String contactId) throws Exception { // 获得所有的号码
		if (TStringUtils.isEmpty(contactId))
			throw new Exception("null by contactId");

		Cursor cursorPhone = getNumbersCursorByContactId(contactId);
		String number = "";

		if (cursorPhone != null && cursorPhone.moveToFirst()) {
			do {
				String phoneName = cursorPhone
						.getString(cursorPhone
								.getColumnIndexOrThrow(Phone.DISPLAY_NAME));
				String phoneNumber = cursorPhone
						.getString(cursorPhone
								.getColumnIndexOrThrow(Phone.NUMBER));

				if (TStringUtils.isEmpty(number)) {
					number = phoneNumber;
				}

			} while (cursorPhone.moveToNext());

			number = TStringUtils.getPhoneNumberStandardization(number);
		}

		if (cursorPhone != null)
			cursorPhone.close();
		return number;
	}

	public String getLocalNumber() throws Exception { // 获取当前的手机号
		TelephonyManager tManager = (TelephonyManager) TApplication
				.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		String number = tManager.getLine1Number();
		return number;
	}

	public Bitmap getAvatarByNumber(String number) throws Exception {
		if (TStringUtils.isEmpty(number))
			throw new Exception("null by number");

		Bitmap bitmap = null;
		long contactId = getContactIdByPhoneNumber(number);
		if (contactId != -1)
			bitmap = getContactsPhotoBitmap(contactId);
		return bitmap;
	}

	public Bitmap getContactsPhotoBitmap(long contactId) throws Exception {
		if (contactId == 0)
			throw new Exception("null by contactId");

		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				contactId);
		InputStream photoStream = Contacts.openContactPhotoInputStream(
				getContentResolver(), contactUri);
		Bitmap photoBitmap = BitmapFactory.decodeStream(photoStream);
		return photoBitmap;
	}

	public long getContactIdByPhoneNumber(String number) throws Exception {
		if (TStringUtils.isEmpty(number))
			throw new Exception("null by number");

		final String[] PHONES_PROJECTION = new String[] { PhoneLookup._ID, };
		final int COLUMN_INDEX_ID = 0;
		long iResult = -1;
		Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		Cursor l_phoneCursor = null;

		try {
			l_phoneCursor = getContentResolver().query(phoneUri,
					PHONES_PROJECTION, null, null, null);
			if (l_phoneCursor != null && l_phoneCursor.moveToFirst()) {
				iResult = l_phoneCursor.getLong(COLUMN_INDEX_ID);
			}
		} catch (Exception e) {
		}

		if (l_phoneCursor != null) {
			l_phoneCursor.close();
		}

		return iResult;
	}

	/**
	 * @param 删除联系人对象
	 */

	public void delSystemContact(String number) {
		Uri contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		Cursor cursor = null;

		try {
			cursor = getContentResolver().query(contactUri, null, null, null,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					String lookupKey = cursor
							.getString(cursor
									.getColumnIndex(Contacts.LOOKUP_KEY));
					Uri uri = Uri.withAppendedPath(
							Contacts.CONTENT_LOOKUP_URI,
							lookupKey);
					getContentResolver().delete(uri, null, null);
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (cursor != null)
			cursor.close();
	}

	// /**
	// * @param 插入联系人对象
	// */
	// public Uri insertSystemContact(Contact contact) throws Exception {
	// Uri result = null;
	// ContentValues contactContentValues = new ContentValues();
	//
	// // 向raw_contacts插入一条除ID外全部为null的数据，ID自动生成
	// long id = ContentUris.parseId(getContentResolver().insert(
	// RawContacts.CONTENT_URI, contactContentValues));
	//
	// // 添加联系人姓名
	// if (!TStringUtils.isEmpty(contact.getName())) {
	// contactContentValues.put(Data.RAW_CONTACT_ID, id);
	// contactContentValues.put(StructuredName.GIVEN_NAME,
	// contact.getName());
	// contactContentValues.put(Data.MIMETYPE,
	// StructuredName.CONTENT_ITEM_TYPE);
	// result = getContentResolver().insert(
	// android.provider.ContactsContract.Data.CONTENT_URI,
	// contactContentValues);
	// contactContentValues.clear();
	// }
	//
	// // 添加联系人电话(帐号)
	// if (!TStringUtils.isEmpty(contact.getNumber())) {
	// contactContentValues
	// .put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
	// id);
	// contactContentValues.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
	// contactContentValues.put(Phone.NUMBER, contact.getNumber());
	// contactContentValues.put(Phone.TYPE, Phone.TYPE_MOBILE);
	// result = getContentResolver().insert(
	// android.provider.ContactsContract.Data.CONTENT_URI,
	// contactContentValues);
	// contactContentValues.clear();
	// }
	//
	// if (!contact.getPhones2().isEmpty()) { // 添加手机2
	// for (String phone : contact.getPhones2()) {
	// contactContentValues
	// .put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
	// id);
	// contactContentValues
	// .put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
	// contactContentValues.put(Phone.NUMBER, phone);
	// contactContentValues.put(Phone.TYPE, Phone.TYPE_MOBILE);
	// result = getContentResolver().insert(
	// android.provider.ContactsContract.Data.CONTENT_URI,
	// contactContentValues);
	// contactContentValues.clear();
	// }
	// }
	//
	// if (!contact.getTelPhones2().isEmpty()) { // 添加电话
	// for (String phone : contact.getTelPhones2()) {
	// contactContentValues
	// .put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
	// id);
	// contactContentValues
	// .put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
	// contactContentValues.put(Phone.NUMBER, phone);
	// contactContentValues.put(Phone.TYPE, Phone.TYPE_HOME);
	// result = getContentResolver().insert(
	// android.provider.ContactsContract.Data.CONTENT_URI,
	// contactContentValues);
	// contactContentValues.clear();
	// }
	// }
	//
	// // 添加联系人邮箱
	// if (!TStringUtils.isEmpty(contact.getEmail())) {
	// contactContentValues
	// .put(android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
	// id);
	// contactContentValues.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
	// contactContentValues.put(Email.DATA, contact.getEmail());
	// contactContentValues.put(Email.TYPE, Email.TYPE_WORK);
	// result = getContentResolver().insert(
	// android.provider.ContactsContract.Data.CONTENT_URI,
	// contactContentValues);
	// contactContentValues.clear();
	// }
	//
	// // 向data表插入QQ数据
	// if (!TStringUtils.isEmpty(contact.getQQ())) {
	// contactContentValues.put(Data.RAW_CONTACT_ID, id);
	// contactContentValues.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
	// contactContentValues.put(Im.DATA, contact.getQQ());
	// contactContentValues.put(Im.PROTOCOL, Im.PROTOCOL_QQ);
	// getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
	// contactContentValues);
	// contactContentValues.clear();
	// }
	//
	// // 添加联系人地址
	// // contactContentValues.put(
	// // android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
	// // id);
	// // contactContentValues.put(Data.MIMETYPE, Address.CONTENT_ITEM_TYPE);
	// // contactContentValues.put(Email.DATA, contact.getEmail());
	// // contactContentValues.put(Email.TYPE, Email.TYPE_WORK);
	// // getContentResolver().insert(
	// // android.provider.ContactsContract.Data.CONTENT_URI,
	// // contactContentValues);
	// // contactContentValues.clear();
	//
	// // 向data表插入头像数据
	// // Bitmap sourceBitmap = BitmapFactory.decodeResource(
	// // mContext.getResources(), R.drawable.icon);
	// // final ByteArrayOutputStream os = new ByteArrayOutputStream();
	// // // 将Bitmap压缩成PNG编码，质量为100%存储
	// // sourceBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
	// // byte[] avatar = os.toByteArray();
	// // contactContentValues.put(Data.RAW_CONTACT_ID, rawContactId);
	// // contactContentValues.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
	// // contactContentValues.put(Photo.PHOTO, avatar);
	// // getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
	// // contactContentValues);
	//
	// return result;
	// }
	//
	// /**
	// * @param 插入联系人对象
	// * 添加联系人，处于同一个事务中
	// */
	// public void insertSystemContact2(Contact contact) throws Exception {
	// ArrayList<ContentProviderOperation> ops = new
	// ArrayList<ContentProviderOperation>();
	// int rawContactInsertIndex = 0;
	//
	// ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
	// .withValue(RawContacts.ACCOUNT_TYPE, null)
	// .withValue(RawContacts.ACCOUNT_NAME, null).build());
	//
	// if (TStringUtils.isEmpty(contact.getName())) { // 名称
	// ops.add(ContentProviderOperation
	// .newInsert(
	// android.provider.ContactsContract.Data.CONTENT_URI)
	// .withValueBackReference(Data.RAW_CONTACT_ID,
	// rawContactInsertIndex)
	// .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
	// .withValue(StructuredName.GIVEN_NAME, "lisi").build());
	// }
	//
	// // 手机帐号/手机/电话
	// ops.add(ContentProviderOperation
	// .newInsert(android.provider.ContactsContract.Data.CONTENT_URI)
	// .withValueBackReference(Data.RAW_CONTACT_ID,
	// rawContactInsertIndex)
	// .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
	// .withValue(Phone.NUMBER, contact.getNumber())
	// .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
	// .withValue(Phone.LABEL, "").build());
	//
	// if (!contact.getPhones2().isEmpty()) {
	// for (String phone : contact.getPhones2()) {
	// ops.add(ContentProviderOperation
	// .newInsert(ContactsContract.Data.CONTENT_URI)
	// .withValueBackReference(
	// ContactsContract.Data.RAW_CONTACT_ID, 0)
	// .withValue(
	// ContactsContract.Data.MIMETYPE,
	// ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
	// .withValue(
	// ContactsContract.CommonDataKinds.Phone.NUMBER,
	// phone)
	// .withValue(
	// ContactsContract.CommonDataKinds.Phone.TYPE,
	// ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
	// .build());
	// }
	// }
	//
	// if (!contact.getTelPhones2().isEmpty()) {
	// for (String phone : contact.getTelPhones2()) {
	// ops.add(ContentProviderOperation
	// .newInsert(ContactsContract.Data.CONTENT_URI)
	// .withValueBackReference(
	// ContactsContract.Data.RAW_CONTACT_ID, 0)
	// .withValue(
	// ContactsContract.Data.MIMETYPE,
	// ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
	// .withValue(
	// ContactsContract.CommonDataKinds.Phone.NUMBER,
	// phone)
	// .withValue(
	// ContactsContract.CommonDataKinds.Phone.TYPE,
	// ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
	// .build());
	// }
	// }
	//
	// // 邮箱
	// if (TStringUtils.isEmpty(contact.getEmail())) {
	// ops.add(ContentProviderOperation
	// .newInsert(ContactsContract.Data.CONTENT_URI)
	// .withValueBackReference(
	// ContactsContract.Data.RAW_CONTACT_ID, 0)
	// .withValue(
	// ContactsContract.Data.MIMETYPE,
	// ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
	// .withValue(ContactsContract.CommonDataKinds.Email.DATA,
	// contact.getEmail())
	// .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
	// ContactsContract.CommonDataKinds.Email.TYPE_WORK)
	// .build());
	// }
	//
	// if (!TStringUtils.isEmpty(contact.getCompany())
	// || !TStringUtils.isEmpty(contact.getPosition())) {// 公司/职位
	// ops.add(ContentProviderOperation
	// .newInsert(ContactsContract.Data.CONTENT_URI)
	// .withValueBackReference(
	// ContactsContract.Data.RAW_CONTACT_ID, 0)
	// .withValue(
	// ContactsContract.Data.MIMETYPE,
	// ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
	// .withValue(
	// ContactsContract.CommonDataKinds.Organization.COMPANY,
	// contact.getCompany())
	// .withValue(
	// ContactsContract.CommonDataKinds.Organization.TYPE,
	// ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
	// .withValue(
	// ContactsContract.CommonDataKinds.Organization.TITLE,
	// contact.getPosition())
	// .withValue(
	// ContactsContract.CommonDataKinds.Organization.TYPE,
	// ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
	// .build());
	// }
	//
	// ContentProviderResult[] results = getContentResolver().applyBatch(
	// ContactsContract.AUTHORITY, ops);
	//
	// ops.clear();
	// ops = null;
	// results = null;
	// }
}
