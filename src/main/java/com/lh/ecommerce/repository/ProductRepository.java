package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.ProductEntity;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
  @Query(
      """
        SELECT NEW ProductEntity(p, i, c)
        FROM ProductEntity p
        JOIN CategoryEntity c ON p.categoryId = c.id
        JOIN ImageEntity i ON p.id = i.productId
        WHERE (p.name ILIKE CONCAT('%', :search, '%')
                OR p.description ILIKE CONCAT('%', :search, '%')
                OR c.name ILIKE CONCAT('%', :search, '%')) and i.isMain=true
        """)
  Page<ProductEntity> search(String search, Pageable pageable);

  @Query(
      """
        select new ProductEntity(
          p, i,
         case when w.id is not null then true else false end,
         case when p.createdAt >= :threshold then true else false end
        )
        from ProductEntity p
        join ImageEntity i on i.productId = p.id and i.isMain = true
        left join WishEntity w on w.productId = p.id and w.userId = :userId
        order by p.createdAt desc
       """)
  List<ProductEntity> getLatestProducts(UUID userId, Pageable pageable, Instant threshold);

  @Query(
      """
        select new ProductEntity(
          p, i,
          true,
          case when p.createdAt >= :threshold then true else false end
        )
        from ProductEntity p
        join ImageEntity i on i.productId = p.id and i.isMain = true
        where p.id in :ids
        order by p.createdAt desc
      """)
  List<ProductEntity> findWishlistProducts(List<UUID> ids, Instant threshold);

  @Query(
      """
        select new ProductEntity(
          p, i,
        case when w.id is not null then true else false end,
        case when p.createdAt >= :threshold then true else false end
        )
        from ProductEntity p
        join CategoryEntity c on p.categoryId = c.id
        join ImageEntity i on p.id = i.productId and i.isMain = true
        left join WishEntity w on w.productId = p.id and w.userId = :userId
        where p.categoryId = :categoryId
      """)
  Page<ProductEntity> findByCategoryId(
      UUID categoryId, UUID userId, Pageable pageable, Instant threshold);

  @Modifying
  @Query(
      """
        update ProductEntity
        set quantity = quantity - :quantity
        where id = :productId and quantity >= :quantity
        """)
  int decreaseQuantity(UUID productId, int quantity);
}
