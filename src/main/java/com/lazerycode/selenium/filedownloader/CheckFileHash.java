/*
 * Copyright (c) 2010-2012 Lazery Attack - http://www.lazeryattack.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lazerycode.selenium.filedownloader;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CheckFileHash {

    private static final Logger LOG = Logger.getLogger(CheckFileHash.class);
    private TypeOfHash typeOfOfHash = null;
    private String expectedFileHash = null;
    private File fileToCheck = null;

    /**
     * The File to perform a Hash check upon
     *
     * @param fileToCheck
     * @throws FileNotFoundException
     */
    public void fileToCheck(File fileToCheck) throws FileNotFoundException {
        if (!fileToCheck.exists()) throw new FileNotFoundException(fileToCheck + " does not exist!");

        this.fileToCheck = fileToCheck;
    }

    /**
     * Hash details used to perform the Hash check
     *
     * @param hash
     * @param typeOfHash
     */
    public void hashDetails(String hash, TypeOfHash typeOfHash) {
        this.expectedFileHash = hash;
        this.typeOfOfHash = typeOfHash;
    }

    /**
     * Performs a expectedFileHash check on a File.
     *
     * @return
     * @throws IOException
     */
    public boolean hasAValidHash() throws IOException {
        if (this.fileToCheck == null) throw new FileNotFoundException("File to check has not been set!");
        if (this.expectedFileHash == null || this.typeOfOfHash == null) throw new NullPointerException("Hash details have not been set!");

        String actualFileHash = "";
        boolean isHashValid = false;

        switch (this.typeOfOfHash) {
            case MD5:
                actualFileHash = DigestUtils.md5Hex(new FileInputStream(this.fileToCheck));
                if (this.expectedFileHash.equals(actualFileHash)) isHashValid = true;
                break;
            case SHA1:
                actualFileHash = DigestUtils.shaHex(new FileInputStream(this.fileToCheck));
                if (this.expectedFileHash.equals(actualFileHash)) isHashValid = true;
                break;
        }

        LOG.info("Filename = '" + this.fileToCheck.getName() + "'");
        LOG.info("Expected Hash = '" + this.expectedFileHash + "'");
        LOG.info("Actual Hash = '" + actualFileHash + "'");

        return isHashValid;
    }

}
