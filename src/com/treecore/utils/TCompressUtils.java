package com.treecore.utils;

import java.io.FileOutputStream;
import java.util.jar.JarOutputStream;

public class TCompressUtils {
	private static String TAG = TCompressUtils.class.getSimpleName();

	public static void compressJar(String filePath) throws Exception {
		// try {
		// // 使用FileOutputStream对象指定一个要输出的压缩文件（file.jar）
		// FileOutputStream fos = new FileOutputStream(filePath);
		// // 第一步:创建JarOutputStream对象
		// JarOutputStream jar = new JarOutputStream(fos);
		// // 第二步：创建一个JarEntry对象，并指定待压缩文件在压缩包中的文件名
		// JarEntry jarEntry = new JarEntry("jarjar.xml");
		// // 第三步：使用putNextEntry方法打开当前的JarEntry对象
		// jar.putNextEntry(jarEntry);
		// InputStream is = getResources().getAssets().open("strings.xml");
		// byte[] buffer = new byte[8192];
		// int count = 0;
		// // 第四步：写入数据
		// while ((count = is.read(buffer)) >= 0) {
		// jar.write(buffer, 0, count);
		// }
		// is.close();
		// // 第五步：关闭当前的jarEntry对象
		// jar.closeEntry();
		// jar.close();
		// } catch (Exception e) {
		// }
	}

	public static void uncompressJar(String filePath) throws Exception {
		// try {
		// // 定义要解压的文件
		// String fileName = android.os.Environment
		// .getExternalStorageDirectory() + "/file.jar";
		// if (!new File(fileName).exists()) {
		// Toast.makeText(this, "压缩文件不存在", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// // 使用FileInputStream对象指定要解压的文件
		// FileInputStream fis = new FileInputStream(fileName);
		// // 第一步：创建JarInputStream对象来读取压缩文件file.jar
		// JarInputStream jis = new JarInputStream(fis);
		// // 第二步：调用getNextJarEntry方法打开压缩包中的第一个文件（如果有多个压缩包，可多次调用该方法）
		// JarEntry jarEntry = jis.getNextJarEntry();
		// // 输出已解压的文件
		// FileOutputStream fos = new FileOutputStream(
		// android.os.Environment.getExternalStorageDirectory() + "/"
		// + jarEntry.getName());
		// byte[] buffer = new byte[8192];
		// int count = 0;
		// while ((count = jis.read()) >= 0) {
		// fos.write(buffer, 0, count);
		// }
		// jis.closeEntry();
		// jis.close();
		// fos.close();
		// Toast.makeText(this, "成功解压jar格式文件", Toast.LENGTH_SHORT).show();
		// } catch (Exception e) {
		// Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		// }
	}

	public static void compressZip(String filePath) {
		// try {
		// // 指定了两个待压缩的文件，都在assets目录中
		// String[] filenames = new String[] { "activity_main.xml",
		// "strings.xml" };
		// FileOutputStream fos = new FileOutputStream(
		// android.os.Environment.getExternalStorageDirectory()
		// + "/file.zip");
		// ZipOutputStream zos = new ZipOutputStream(fos);
		// int i = 1;
		// // 枚举filenames中的所有待压缩文件
		// while (i <= filenames.length) {
		// // 从filenames数组中取出当前待压缩的文件名，作为压缩后的名称，以保证压缩前后文件名一致
		// ZipEntry zipEntry = new ZipEntry(filenames[i - 1]);
		// // 打开当前的zipEntry对象
		// zos.putNextEntry(zipEntry);
		//
		// InputStream is = getResources().getAssets().open(
		// filenames[i - 1]);
		// byte[] buffer = new byte[8192];
		// int count = 0;
		// // 写入数据
		// while ((count = is.read(buffer)) >= 0) {
		// zos.write(buffer, 0, count);
		// }
		// zos.flush();
		// zos.closeEntry();
		// is.close();
		// i++;
		//
		// }
		// zos.finish();
		// zos.close();
		// } catch (Exception e) {
		// }
	}

	public static void uncompressZip(String filePath) {
		// try {
		// // 指定待解压的文件
		// String filename = android.os.Environment
		// .getExternalStorageDirectory() + "/file.zip";
		// if (!new File(filename).exists()) {
		// Toast.makeText(this, "压缩文件不存在.", Toast.LENGTH_LONG).show();
		// return;
		// }
		// FileInputStream fis = new FileInputStream(filename);
		// ZipInputStream zis = new ZipInputStream(fis);
		// ZipEntry zipEntry = null;
		// // 通过不断调用getNextEntry方法来解压file.zip中的所有文件
		// while ((zipEntry = zis.getNextEntry()) != null) {
		// FileOutputStream fos = new FileOutputStream(
		// android.os.Environment.getExternalStorageDirectory()
		// + "/" + zipEntry.getName());
		//
		// byte[] buffer = new byte[8192];
		// int count = 0;
		// while ((count = zis.read(buffer)) >= 0) {
		// fos.write(buffer, 0, count);
		// }
		// zis.closeEntry();
		// fos.close();
		// }
		// zis.close();
		// } catch (Exception e) {
		// }
	}

}
