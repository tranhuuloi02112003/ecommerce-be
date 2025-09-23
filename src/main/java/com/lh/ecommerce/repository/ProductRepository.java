package com.lh.ecommerce.repository;

import com.lh.ecommerce.entity.ProductEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
     select new ProductEntity(p, i)
     from ProductEntity p
     join ImageEntity i on p.id = i.productId
     where i.isMain = true
     order by p.createdAt desc
""")
  List<ProductEntity> getExploreProducts(Pageable pageable);
}
