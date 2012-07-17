package com.lazerycode.selenium.filedownloader;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class HashCheckFile {

    public static enum hashType {
        MD5,
        SHA1;
    }

    private hashType typeOfHash;
    private String hash;
    private File fileToCheck;

    public HashCheckFile(File fileToCheck, String hash, hashType hashType) throws FileNotFoundException{
        fileToCheck(fileToCheck);
        hashDetails(hash, hashType);
    }

    public void fileToCheck(File fileToCheck) throws FileNotFoundException{
        if(!fileToCheck.exists()) throw new FileNotFoundException(fileToCheck + " does not exist!");
        this.fileToCheck = fileToCheck;
    }

    public void hashDetails(String hash, hashType hashType){
        this.hash = hash;
        this.typeOfHash = hashType;
    }

    public boolean hasAValidHash() throws FileNotFoundException, IOException{
        switch(this.typeOfHash){
            case MD5:
                if (this.hash.equals(DigestUtils.md5Hex(new FileInputStream(this.fileToCheck)))) return true;
                break;
            case SHA1:
                if (this.hash.equals(DigestUtils.shaHex(new FileInputStream(this.fileToCheck)))) return true;
                break;
            default:
                return false;
        }
        return false;
    }

}
