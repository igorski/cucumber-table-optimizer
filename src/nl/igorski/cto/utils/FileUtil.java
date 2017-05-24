/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Igor Zinken
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package nl.igorski.cto.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class FileUtil
{
    public static boolean createFoldersForPath( String path ) {
        final File file = new File( path );
        return file.getParentFile().mkdirs();
    }

    public static String[] getFileContentsAsArray( File file ) {

        try {
            final Path filePath   = file.toPath();
            final Charset charset = Charset.defaultCharset();
            final List<String> stringList = Files.readAllLines( filePath, charset );

            return stringList.toArray( new String[]{} );
        }
        catch ( IOException e ) {
            return new String[]{};
        }
    }

    public static void generateFile( String fileName, ArrayList<String> data,  File output ) {

        try {
            Path outputFile = Paths.get( output + File.separator + fileName );
            FileUtil.createFoldersForPath( outputFile.toAbsolutePath().toString() );
            Files.write( outputFile, data, Charset.forName( "UTF-8" ));
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
