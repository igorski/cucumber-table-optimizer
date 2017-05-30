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
package nl.igorski.cto;

import nl.igorski.cto.utils.*;
import java.io.File;

public final class Main
{
    /**
     * @param args expects 2 command line argumentsm the first is the input directory
     *        to recursively traverse for Cucumber files, the second is the output directory
     *        in which to write the transformed Cucumber files
     */
    public static void main( String[] args )
    {
        // process command line arguments

        if ( args == null || args.length < 2 ) {
            System.out.println( "expected both input and output folders to be specified." );
            System.exit( -1 );
        }

        final String input  = args[0];
        final String output = args[1];

        // grab input directory and check its existance

        final File inputDirectory = new File( input );
        if ( !inputDirectory.exists() || !inputDirectory.isDirectory()) {
            System.out.println( "directory '" + input + "' does not exist." );
            System.exit( -1 );
        }

        // create output directory (and recursively create its parent folders)

        final File outputDirectory = new File( output );
        FileUtil.createFoldersForPath( output );

        System.out.println( "optimizing Cucumber files in '" + input + "' into '" + output + "'" );

        CucumberProcessor.walk( inputDirectory, outputDirectory );
    }
}
