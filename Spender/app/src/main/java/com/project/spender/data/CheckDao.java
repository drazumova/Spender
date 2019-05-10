package com.project.spender.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;

import java.util.List;

@Dao
public abstract class CheckDao {

    @Query("SELECT * FROM Product")
    public abstract List<Product> getAllProducts();

    @Query("SELECT * FROM `Check`")
    public abstract List<Check> getAllChecks();

    @Transaction
    @Query("SELECT * FROM `Check`")
    public abstract List<CheckWithProducts> getAll();

    @Insert
    public abstract long insertCheck(Check check);

    @Insert
    public abstract long insertProduct(Product product);

    @Transaction
    public void insertCheckWithProducts(CheckWithProducts checkWithProducts) {
        long newIndex = insertCheck(checkWithProducts.getCheck());
        checkWithProducts.updateCheckId(newIndex);
        for (Product product : checkWithProducts.getProducts()) {
            insertProduct(product);
        }
    }
}
