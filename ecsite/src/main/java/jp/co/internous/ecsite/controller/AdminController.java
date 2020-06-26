//Java Servletと類似の働きをするJavaクラスを格納
//ページ遷移、model/view間の橋渡しの役割を担う
//対応するフロントエンドhtmlと交互に作成することで、動作・表示を確認しながら開発が進めることができる
package jp.co.internous.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.internous.ecsite.model.dao.GoodsRepository;
import jp.co.internous.ecsite.model.dao.UserRepository;
import jp.co.internous.ecsite.model.entity.Goods;
import jp.co.internous.ecsite.model.entity.User;
import jp.co.internous.ecsite.model.form.GoodsForm;
import jp.co.internous.ecsite.model.form.LoginForm;

@Controller
@RequestMapping("/ecsite/admin")	//localhost:8080/ecsite/admin/のURLで アクセスできるよう設定
public class AdminController {

//UerRepositoryを読み込み
	@Autowired
	private UserRepository userRepos;
//GoodsRepositoryを読み込み
	@Autowired
	private GoodsRepository goodsRepos;
	
//トップページに遷移するメソッド
	@RequestMapping("/")
	public String index() {
		return "adminindex";	
	}
//LoginFormを使ってユーザー情報を受け取るメソッドを追加	
	@PostMapping("/welcome")
//ユーザー名とパスワードでユーザーを検索
	public String welcome(LoginForm form, Model m) {
		List<User> users=userRepos.findByUserNameAndPassword(form.getUserName(),form.getPassword());
	
//検索結果が存在していれば、isAdmin(管理者かどうか)を取得し、管理者だった場合のみ処理
		if(users !=null && users.size() >0) {
			boolean isAdmin = users.get(0).getIsAdmin() !=0;
			if(isAdmin) {
				List<Goods> goods=goodsRepos.findAll();
				m.addAttribute("userName",users.get(0).getUserName());
				m.addAttribute("password",users.get(0).getPassword());
				m.addAttribute("goods",goods);
			}
		}
		
		return "welcome";
	}
	
//新規商品登録する機能を実装 goodsMstメソッドを追加　goodsmstのフロントページへ遷移
	@RequestMapping("/goodsMst")
	public String goodsMst(LoginForm form, Model m) {
		m.addAttribute("userName",form.getUserName());
		m.addAttribute("password",form.getPassword());
		
		return "goodsmst";
	}

//新規商品登録する機能を実装 addGoodsメソッドを追加 GoodsFormクラスから情報を受け取る
	@RequestMapping("/addGoods")
	public String addGoods(GoodsForm goodsForm, LoginForm loginForm, Model m) {
		m.addAttribute("userName", loginForm.getUserName());
		m.addAttribute("password", loginForm.getPassword());
		
		Goods goods=new Goods();
		goods.setGoodsName(goodsForm.getGoodsName());
		goods.setPrice(goodsForm.getPrice());
		goodsRepos.saveAndFlush(goods);
		
		return "forward:/ecsite/admin/welcome";
	}
	
//商品マスタから商品を削除する機能を作成　ajaxを使用した方式での処理(Rest)。
	@ResponseBody
	@PostMapping("/api/deleteGoods")	//ajaxを使用しているのでapiがつく
//deleteApiメソッドを追加
	public String deleteApi(@RequestBody GoodsForm f, Model m) {
		try {
			goodsRepos.deleteById(f.getId());
		}catch(IllegalArgumentException e) {
			return "-1";
		}
		return "1";
	}
}