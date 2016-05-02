package net.treewoods.sample_common_compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;

public class TarUtil {
	
	// 指定したディレクトリ内のファイルをアーカイブ
	public void archive(String baseDir, String outputFile) throws FileNotFoundException, IOException {
		// baseDir内のファイル一覧を取得
		Collection<File> listFiles = FileUtils.listFiles(new File(baseDir), null, true);
		String[] inputFiles = listFiles.stream().map(t -> {
			String p = t.getPath();
			return p.substring(baseDir.length(), p.length());
		}).toArray(count -> {return new String[count];});
		
		this.archive(baseDir, inputFiles, outputFile);
	}

	//作成
	public void archive(String baseDir, String[] inputFiles, String outputFile) throws FileNotFoundException, IOException {
		try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(outputFile))) {
			for (String inputFile : inputFiles) {
				File f = new File(baseDir + inputFile);
				out.putArchiveEntry(new TarArchiveEntry(f, inputFile));
				out.write(org.apache.commons.io.FileUtils.readFileToByteArray(f));
				out.closeArchiveEntry();
			}
		}
	}

	//展開
	public void extract(String inputFile, String outputDir) throws FileNotFoundException, IOException {
		try (TarArchiveInputStream in = new TarArchiveInputStream(new FileInputStream(inputFile))) {
			BufferedInputStream bis = new BufferedInputStream(in);
			ArchiveEntry entry;
			while ((entry = in.getNextEntry()) != null) {
				File outFile = new File(outputDir + "/" + entry.getName());
				
				// ディレクトリならディレクトリ作成
				if (entry.isDirectory()) {
					outFile.mkdirs();
					continue;
				}
				
				// 親のディレクトリがなければ作成
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdirs();
				}

				//FileUtils.copyInputStreamToFile(part.getInputStream(), file);
				try (FileOutputStream fos = new FileOutputStream(outFile);
						BufferedOutputStream bos = new BufferedOutputStream(fos)) {
					// エントリの出力
					int i;
					while ((i = bis.read()) != -1) {
						bos.write(i);
					}
				}
			}
		}
	}
}
