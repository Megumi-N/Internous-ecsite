//ページ遷移、model/view間の橋渡しの役割を担う
package jp.co.internous.ecsite.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.ecsite.model.dao.GoodsRepository;
import jp.co.internous.ecsite.model.dao.PurchaseRepository;
import jp.co.internous.ecsite.model.dao.UserRepository;
import jp.co.internous.ecsite.model.dto.HistoryDto;
import jp.co.internous.ecsite.model.dto.LoginDto;
import jp.co.internous.ecsite.model.entity.Goods;
import jp.co.internous.ecsite.model.entity.Purchase;
import jp.co.internous.ecsite.model.entity.User;
import jp.co.internous.ecsite.model.form.CartForm;
import jp.co.internous.ecsite.model.form.HistoryForm;
import jp.co.internous.ecsite.model.form.LoginForm;

@Controller
@RequestMapping("/ecsite")	//local:8080/ecsiteのURLでアクセスできるよう指定
public class IndexController {

//User EntityからuserテーブルにアクセスするDAO
	@Autowired
	private UserRepository userRepos;
	
//Goods EntityからgoodsテーブルにアクセスするDAO
	@Autowired
	private GoodsRepository goodsRepos;
	
	@Autowired
	private PurchaseRepository purchaseRepos;

//WebサービスApiとして作成するためにJson形式を扱えるようにGsonをインスタンス化しておく
	private Gson gson = new Gson();

//トップページ"index"に遷移するメソッド。goodsテーブルから取得した商品Entityの一覧をフロントに渡す役割を持つmodelに追加
	@RequestMapping("/")
	public String index (Model m) {
		List<Goods> goods=goodsRepos.findAll();
		m.addAttribute("goods", goods);
		
		return "index";
	}
	
//ログイン機能を追加
	@ResponseBody
	@PostMapping("/api/login")	//DBテーブル(user)からユーザー名とパスワードで検索し、結果を取得
	public String loginApi(@RequestBody LoginForm form) { 
		List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(), form.getPassword());
		
//DTOをゲストの情報で初期化、検索結果が存在していた場合のみ実在のユーザー情報をDTOに詰めている
		LoginDto dto = new LoginDto(0, null, null, "ゲスト");
		if(users.size() > 0) {
			dto = new LoginDto(users.get(0));
		}
//DTOをJsonオブジェクトとして画面側に返す
		return gson.toJson(dto);
	}

//購入処理を担うメソッドの追加
	@ResponseBody
	@PostMapping("/api/purchase")
	public String purchaseApi(@RequestBody CartForm f) {
		
		f.getCartList().forEach((c) -> {
			long total = c.getPrice() * c.getCount();
			purchaseRepos.persist(f.getUserId(), c.getId(), c.getGoodsName(), c.getCount(), total);
		});
		
		return String.valueOf(f.getCartList().size());
	}

//購入履歴を表示するメソッドを追加
	@ResponseBody
	@PostMapping("/api/history")
	public String historyApi(@RequestBody HistoryForm form) {
		String userId = form.getUserId();
		List<Purchase> history = purchaseRepos.findHistory(Long.parseLong(userId));
		List<HistoryDto> historyDtoList = new ArrayList<>();
		history.forEach((v) -> {
			HistoryDto dto = new HistoryDto(v);
			historyDtoList.add(dto);
		});
		
		return gson.toJson(historyDtoList);
	}
	
}