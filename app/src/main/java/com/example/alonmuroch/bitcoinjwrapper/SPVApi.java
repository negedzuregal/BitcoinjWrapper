package com.example.alonmuroch.bitcoinjwrapper;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.KeyChain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alonmuroch on 4/12/15.
 */
public class SPVApi {

    static SPVApi instance;
    public static  SPVApi sharedInstance() {
        if(instance == null) {
            instance = new SPVApi();
        }

        return instance;
    }

    public Coin getBalance() {
        Wallet w = SPVFacade.sharedInstance().getWallet();
        return w.getBalance();
    }

    public Address getFirstAddress() {
        return getAddress(0);
    }

    public Wallet.SendResult sendToAddress(Address address, Coin amount) throws InsufficientMoneyException {
        return send(address, amount);
    }


    // private
    
    private Address getAddress(int idx) {
        Wallet w = SPVFacade.sharedInstance().getWallet();

        ChildNumber purpose = new ChildNumber(KeyChain.KeyPurpose.RECEIVE_FUNDS.ordinal(), true);
        ChildNumber type = new ChildNumber(0);
        ChildNumber addressIdx = new ChildNumber(idx);

        List<ChildNumber> cp = new ArrayList<>();
        cp.add(purpose); cp.add(type); cp.add(addressIdx);

        DeterministicKey key = w.getKeyByPath(cp);
        Address address = key.toAddress(w.getParams());
        return address;
    }

    private Wallet.SendResult send(Address address, Coin amount) throws InsufficientMoneyException {
        Wallet w = SPVFacade.sharedInstance().getWallet();
        Wallet.SendRequest sr =  Wallet.SendRequest.to(address, amount);
        return w.sendCoins(sr);
    }
}