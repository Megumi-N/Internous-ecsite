//購入情報をフロントからJava側に渡す
//カート情報はカートに入れた商品のリストとして渡ってくるため、Cartのリストを保持する役目を持つCartFormが存在
package jp.co.internous.ecsite.model.form;

import java.io.Serializable;

public class Cart implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String goodsName;
	private long price;
	private long count;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id=id;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName=goodsName;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price=price;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count=count;
	}
}
