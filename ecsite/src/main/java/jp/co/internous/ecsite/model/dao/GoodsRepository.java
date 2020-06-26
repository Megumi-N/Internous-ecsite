//modelはデータベースとのやり取りに使用される情報や、
//フロントとパック間でのやりとりに使用される情報などを役割に応じて格納するための親パッケージ

//Data Access Objectの略。modelの中でもDBにアクセスする役割を持つクラスインターフェース。
package jp.co.internous.ecsite.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.internous.ecsite.model.entity.Goods;
//全商品をgoodsテーブルから検索
public interface GoodsRepository extends JpaRepository<Goods, Long> {
}