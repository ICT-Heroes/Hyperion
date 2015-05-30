package model;

public class Clsx {
	private String name;
	public Clsx[] item;
	
	public Clsx(String name){
		this.name = name;
		item = null;
	}
	
	public Clsx(){
		this("");
	}
	
	public Clsx[] getItem() {
		return item;
	}

	public void setItem(Clsx[] item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Clsx GetItem(int index){
		int curIndex = index;
		if(GetItemCount() < index){
			return new Clsx("null");
		}else{
			for(int i = 0 ; i < item.length ; i ++){
				curIndex -= item[i].GetItemCount();
				if(curIndex < 0){
					curIndex += item[i].GetItemCount();
					return item[i].GetItem(curIndex);
				}else if(curIndex == 0){
					return item[i];
				}
			}
		}
		return new Clsx();
	}
	
	/*
	 * Clsx 가 갖는 아이템들의 갯수를 리턴
	 */
	public int GetItemCount(){
		int length = 0;
		int ret = 0;
		if(item != null){	length = item.length;	}
		if(length == 0){	ret = 1;	}
		else{
			for(int i = 0 ; i < length ; i ++){
				ret += item[i].GetItemCount();
			}
		}
		return ret;
	}

}
