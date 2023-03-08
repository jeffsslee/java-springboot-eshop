package com.example.eshop.repository;

import com.example.eshop.constant.ItemSellStatus;
import com.example.eshop.dto.ItemSearchDto;
import com.example.eshop.dto.MainItemDto;
import com.example.eshop.dto.QMainItemDto;
import com.example.eshop.entity.Item;
import com.example.eshop.entity.QItem;
import com.example.eshop.entity.QItemImg;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

  private JPAQueryFactory queryFactory;

  public ItemRepositoryCustomImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
    return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
  }

  private BooleanExpression regDateAfter(String searchDateType) {
    LocalDateTime dateTime = LocalDateTime.now();

    if (StringUtils.equals("all", searchDateType) || searchDateType == null) {
      return null;
    } else if (StringUtils.equals("1d", searchDateType)) {
      dateTime = dateTime.minusDays(1);
    } else if (StringUtils.equals("1w", searchDateType)) {
      dateTime = dateTime.minusWeeks(1);
    } else if (StringUtils.equals("1m", searchDateType)) {
      dateTime = dateTime.minusMonths(1);
    } else if (StringUtils.equals("6m", searchDateType)) {
      dateTime = dateTime.minusMonths(6);
    }

    return QItem.item.regTime.after(dateTime);
  }

  private BooleanExpression searchByLike(String searchBy, String searchQuery) {
    if (StringUtils.equals("itemName", searchBy)) {
      return QItem.item.itemName.like("%" + searchQuery + "%");
    } else if (StringUtils.equals("createdBy", searchBy)) {
      return QItem.item.createdBy.like("%" + searchQuery + "%");
    }
    return null;
  }

  @Override
  public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

    List<Item> content = queryFactory
        .selectFrom(QItem.item)
        .where(
            regDateAfter(itemSearchDto.getSearchDateType())
            , searchSellStatusEq(itemSearchDto.getSearchSellStatus())
            , searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
        )
        .orderBy(QItem.item.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count).from(QItem.item)
        .where(
            regDateAfter(itemSearchDto.getSearchDateType())
            , searchByLike(
                itemSearchDto.getSearchBy()
                , itemSearchDto.getSearchQuery()
            )
        )
        .fetchOne();

    return new PageImpl<>(content, pageable, total);
  }

  private BooleanExpression itemNameLike(String searchQuery) {
    return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemName.like("%" + searchQuery + "%");
  }

  @Override
  public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

    QItem item = QItem.item;
    QItemImg itemImg = QItemImg.itemImg;

    List<MainItemDto> content = queryFactory
        .select(
            new QMainItemDto(item.id, item.itemName, item.itemDetail, itemImg.imgUrl, item.price)
        )
        .from(itemImg)
        .join(itemImg.item, item)
        .where(itemImg.repImgYn.eq("Y"))
        .where(itemNameLike(itemSearchDto.getSearchQuery()))
        .orderBy(item.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long total = queryFactory
        .select(Wildcard.count)
        .from(itemImg)
        .join(itemImg.item, item)
        .where(itemImg.repImgYn.eq("Y"))
        .where(itemNameLike(itemSearchDto.getSearchQuery()))
        .fetchOne();

    return new PageImpl<>(content, pageable, total);
  }
}
