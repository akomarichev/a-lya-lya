package vk.api;

public class Auth {
    public static String getSettings(){
        int settings=1+2+4+8+16+32+64+128+1024+2048+4096+8192+65536+131072+262144;
        return Integer.toString(settings);
    }
}