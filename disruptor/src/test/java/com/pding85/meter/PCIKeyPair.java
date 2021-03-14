package com.pding85.meter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: dzy
 * @Date: 2018/9/27 14:18
 * @Describe: 公私钥对
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PCIKeyPair {

    private String priKey;      //私钥
    private String pubKey;      //公钥

}