package net.treewoods.sample_common_compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class TarUtil {

    // ArchiveStreamFactoryを使うとzip等もいける？
    
    /**
     * 指定したディレクトリ内のファイルをtarにアーカイブ。
     * @param baseDir 指定のディレクトリ
     * @param targetExtensions 対象の拡張子。{"txt", "html"}のように指定する。すべてのファイルを指定する場合null。
     * @param outputFile 出力tarファイル名
     * @param recursive trueの場合、サブディレクトリのファイルも対象となる。
     * @throws IOException 
     */
    public void archiveFromDir(String baseDir, String[] targetExtensions,
            String outputFile, boolean recursive) throws IOException {
        // baseDir内のファイル一覧を取得
        Collection<File> listFiles = FileUtils.listFiles(new File(baseDir), targetExtensions, recursive);
        String[] inputFiles = listFiles.stream().map(t -> {
            String p = t.getPath();
            return p.substring(baseDir.length(), p.length());
        }).toArray(count -> {
            return new String[count];
        });

        this.archiveFromFiles(baseDir, inputFiles, outputFile);
    }

    /**
     * 指定したファイルをtarにアーカイブ
     * @param baseDir 指定のディレクトリ
     * @param inputFiles アーカイブするファイル
     * @param outputFile 出力tarファイル名
     * @throws IOException 
     */
    public void archiveFromFiles(String baseDir, String[] inputFiles, String outputFile) throws IOException {
        try (TarArchiveOutputStream out = new TarArchiveOutputStream(new FileOutputStream(outputFile))) {
            for (String inputFile : inputFiles) {
                File f = new File(baseDir + inputFile);
                out.putArchiveEntry(new TarArchiveEntry(f, inputFile));
                out.write(org.apache.commons.io.FileUtils.readFileToByteArray(f));
                out.closeArchiveEntry();
            }
        }
    }

    /**
     * 指定したtarファイルを展開
     * @param inputFile 展開するtarファイル
     * @param outputDir 出力ディレクトリ
     * @throws IOException 
     */
    public void extract(String inputFile, String outputDir) throws IOException {
        try (TarArchiveInputStream is = new TarArchiveInputStream(new FileInputStream(inputFile));
                BufferedInputStream bis = new BufferedInputStream(is)) {
            ArchiveEntry entry;
            while ((entry = is.getNextEntry()) != null) {
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

                try (FileOutputStream fos = new FileOutputStream(outFile);
                        BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                    // エントリの出力
                    IOUtils.copy(bis, bos);
                }
            }
        }
    }
}
