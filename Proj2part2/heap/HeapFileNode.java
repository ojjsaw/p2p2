package heap;

import chainexception.ChainException;
import heap.HFPage;

import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;
import global.RID;

public class HeapFileNode extends HFPage{
	
	HeapFileNode prev = null;
	HeapFileNode next = null;
	Page page;
	PageId pageno;
	
	public HeapFileNode(Page page, PageId pageno){
		this.page = page;
		this.pageno = pageno;
	}
	
	HeapFileNode getNext(){
		return next;
	}
	
	HeapFileNode getPrev(){
		return prev;
	}
	
	void setPrev(HeapFileNode p){
		prev = p;
	}
	
	void setNext(HeapFileNode n){
		next = n;
	}
	
}
