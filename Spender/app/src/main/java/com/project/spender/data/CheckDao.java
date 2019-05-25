package com.project.spender.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.project.spender.data.entities.Check;
import com.project.spender.data.entities.CheckWithProducts;
import com.project.spender.data.entities.Product;
import com.project.spender.data.entities.ProductTagJoin;
import com.project.spender.data.entities.ProductWithTags;
import com.project.spender.data.entities.Tag;

import java.util.List;
import java.util.Set;

/**
 * Абстрактный класс описывает правила общения с бд.
 * Умеет интегрироваться с LiveData (надо бы сделать)
 * Без LiveData по умолчанию вызов любой операции в main потоке кидает Exception (зависит от бд см AppDatabase).
 */
@Dao
public abstract class CheckDao {

    // INSERT

    /**
     * Добавляет новый чек в бд. Если id > 0 и такой id уже есть, то кидает Exception.
     * Не изменяет переданный объект.
     *
     * @param check добавляемы чек.
     * @return новый id или 0 если элемент не был добавлен.
     */
    @Insert
    public abstract long insertCheck(Check check);

    /**
     * Добавляет новый товар в бд. Если id > 0 и такой id уже есть, то кидает Exception.
     * Если в таблице нет чека с id == check_id, то кидает  Exception.
     * Не изменяет переданный объект.
     *
     * @param product добавляемый товар.
     * @return новый id или 0 если элемент не был добавлен.
     */
    @Insert
    public abstract long insertProduct(Product product);

    /**
     * Добавляет тег. Ничего не делает если такой есть.
     * Имя тега, как и id уникальный.
     *
     * @param tag добавляемый тег.
     * @return новый id или 0 если такой есть.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertTag(Tag tag);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract void insertProductTagJoin(ProductTagJoin productTagJoin);

    /**
     * Добавляет в бд чек со всеми товарами. Обновляет id всех добавленных объектов.
     * В случае неудачи кидает Exception.
     *
     * @param checkWithProducts добавляемый чек с товарами.
     */
    @Transaction
    public void insertCheckWithProducts(CheckWithProducts checkWithProducts) {
        long newIndex = insertCheck(checkWithProducts.getCheck());
        checkWithProducts.updateCheckId(newIndex);
        for (Product product : checkWithProducts.getProducts()) {
            product.setId(insertProduct(product));
        }
    }

    /**
     * Добавляет в бд тег для товара с данным id.
     * Если тег есть, ничего не делает.
     *
     * @param tag добавляемый тег.
     * @param productId id соответствующего товара.
     */
    @Transaction
    public long insertTagForProduct(Tag tag, long productId) {
        long tagId = (tag.getId() > 0) ? tag.getId() : insertTag(tag);
        if (tagId <= 0) {
            tagId = getTagId(tag.getName());
        }
        insertProductTagJoin(new ProductTagJoin(productId, tagId));
        return tagId;
    }

    /**
     * Добавляет лист тегов для товара с данным id.
     * Если тег есть, ничего не делает.
     *
     * @param tags добавляемый тег.
     * @param productId id соответстующего товара.
     */
    @Transaction
    public void insertTagsForProduct(List<Tag> tags, long productId) {
        for (Tag tag : tags) {
            insertTagForProduct(tag, productId);
        }
    }

    /**
     * Добавляет продукт со списком тегов.
     * В отличие от insertCheckWithProducts не обновляет id в переданных объектах (хз как лучше)
     * @param productWithTags продукт с списком тегов
     */
    @Transaction
    public void insertProductWithTags(ProductWithTags productWithTags) {
        long productId = insertProduct(productWithTags.getProduct());
        insertTagsForProduct(productWithTags.getTags(), productId);
    }

    // GET
    // todo getByTag and getByTime

    /**
     * Метод получает все товары из бд без информации о чеках.
     *
     * @return все товары
     */
    @Query("SELECT * FROM Product")
    public abstract List<Product> getAllProducts();

    /**
     * Returns list of all tags
     */
    @Query("SELECT * FROM Tag")
    public abstract List<Tag> getAllTags();


    /**
     * Метод получает все чеки из бд без информации о товарах.
     *
     * @return все чеки
     */
    @Query("SELECT * FROM `Check`")
    public abstract List<Check> getAllChecks();

    /**
     * Метод получает все чеки вместе со всеми товарами в них.
     *
     * @return все чеки с информацией о товарах
     */
    @Transaction
    @Query("SELECT * FROM `Check`")
    public abstract List<CheckWithProducts> getAll();

    /**
     * Возвращает id тега по имени.
     *
     * @param name имя тега.
     * @return id тега или 0 если такого нет.
     */
    @Query("SELECT id FROM tag WHERE name = :name")
    public abstract long getTagId(String name);

    @Transaction
    @Query("SELECT tag.id, tag.name, tag.color FROM tag INNER JOIN product_tag_join ON tag.id = tag_id WHERE product_id == :productId")
    public abstract List<Tag> getTagsByProductId (long productId);

    @Transaction
    @Query("SELECT DISTINCT tag.id, tag.name, tag.color FROM tag, product_tag_join, product WHERE tag.id = tag_id AND product_id == product.id AND check_id = :checkId")
    public abstract List<Tag> getTagsByCheckId (long checkId);

    /**
     * Получает последний вставленный id.
     *
     * @return послединий id.
     */
    @Query("SELECT last_insert_rowid()")
    public  abstract long getLastId();

    // DELETE. Все зависимые объекты удаляются автоматически. Например все товары из чека.
    // Теги являются независимыми, поэтому их иногда нужно чистить вручную.

    @Query("DELETE FROM `check` WHERE id = :id")
    public abstract void deleteCheckById(long id);

    @Query("DELETE FROM product WHERE id = :id")
    public abstract void deleteProductById(long id);

    @Query("DELETE FROM tag WHERE id = :id")
    public abstract void deleteTagById(long id);

    @Query("DELETE FROM tag WHERE name = :name")
    public abstract void deleteTagByName(String name);

    /**
     * Удаляет все неиспользуемые теги.
     */
    @Transaction
    @Query("DELETE FROM tag WHERE id NOT IN (SELECT tag_id FROM product_tag_join)")
    public  abstract void deleteAllUnusedTags();
}
