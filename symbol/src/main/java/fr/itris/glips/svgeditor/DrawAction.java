package fr.itris.glips.svgeditor;

import java.awt.event.ActionEvent;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class DrawAction extends AbstractHyperlinkAction<DrawUnit>{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String searchName;
	
	private long clickrecord;		//上一次点击时间
	
	private long currentclick;		//当前点击时刻

	public DrawAction(DrawUnit drawUnit) {

		super(drawUnit);
		 this.searchName = getPinYinHeadChar(drawUnit.getName());
	}

	@Override
     protected void installTarget() {
		
         if (getTarget() == null) return;
         setSmallIcon(getTarget().getIcon());
         setName(getTarget().getName());
//         setShortDescription(getTarget().getShortDescription());
     }

	 /**
	  * 点击触发
	  */
	@Override
	public void actionPerformed(ActionEvent evt) {

		setVisited(true);
        currentclick = System.currentTimeMillis();
        
//        System.out.println(getTarget().getName() + "\t" + currentclick);
        if(System.currentTimeMillis() - clickrecord < 350)
        {
        	System.out.println("鼠标双击");
        	//双击事件
        	openSymbol();
        }
        clickrecord = currentclick;
	}
	
	public  void openSymbol()
	{
		
		String svgstr = SymbolMap.shapemap.get(getTarget().getId());

		fr.itris.glips.svgeditor.io.IOManager  iomanager =  Editor.getEditor().getIOManager();
		
		iomanager.getFileOpenManager().open(svgstr, getTarget().getName(), null, getTarget().getId());
		
	}

	/**
	 * 关键词搜索
	 * @param keyword
	 * @return
	 */
	public boolean isMatch(String keyword)
	{
//		System.out.println(keyword + "\t" + searchName);
		if(keyword!=null && searchName.contains(keyword))
		{
			return true;
		}
		return false;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	
	//返回中文
	public static String getPingYin(String src){
		char[] t1 = null;
		t1=src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4="";
		int t0=t1.length;
		try {
			for (int i=0;i<t0;i++)
			{
				//判断是否为汉字字符
				if(java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+"))
				{
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
					t4+=t2[0];
				}
				else
					t4+=java.lang.Character.toString(t1[i]);
			}
//		       System.out.println(t4);
			return t4;
		}
		catch (BadHanyuPinyinOutputFormatCombination e1) {
			e1.printStackTrace();
		}
		return t4;
	}
	
	//返回中文的首字母
	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			}else {
				convert += word;
			}
		}
		return convert;
	}
	
	public static void main(String[] args) {
		String cnStr = "abc_中华人民共和国";
		System.out.println(getPingYin(cnStr));
		System.out.println(getPinYinHeadChar(cnStr));
	}
}
