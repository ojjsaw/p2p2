package heap;

import chainexception.ChainException;
import heap.HFPage;

import global.GlobalConst;
import global.Minibase;
import global.Page;
import global.PageId;
import global.RID;

public class HeapFile {

	PageId hpid;
	HeapFileNode hfpage = null;
/**
   * If the given name already denotes a file, this opens it; otherwise, this
   * creates a new empty file. A null name produces a temporary heap file which
   * requires no DB entry.
   */
  public HeapFile(String name) {
	  
	  PageId pageno = null;
	  Page page;
	  
	  pageno = Minibase.DiskManager.get_file_entry(name);
	  
	  if(pageno == null) {//produce a temporary heap file
		  pageno = new PageId();
		  page = new Page();
		  pageno = Minibase.BufferManager.newPage(page, 1);
		  Minibase.BufferManager.unpinPage(pageno, true);
		  hfpage = new HeapFileNode(page, pageno);
		  if(hfi.top == null){
			  hfi.top = hfpage;
		  }
		  //Minibase.DiskManager.add_file_entry(name, pageno);//A null name produces a temporary heap file which requires no DB entry.
		  
		  //Can we use HFPage? not sure if needed
		  
		  /*hfpage.setNextPage(pageno);
		  System.out.println("Before: " + hfpage.getCurPage());
		  hfpage.setNextPage(pageno);
		  System.out.println("after: " + hfpage.getCurPage());*/
	  }
	  else{
		  //TODO:open the file - openDB? Where is DB created?
		  page = new Page();
		  //Cant create HFPage without page
		  //hfpage = new HFPage(page);
		  hpid = new PageId();
		  hpid.pid = pageno.pid;
		  //Minibase.DiskManager.read_page(pageno, page);//Read data in existing page
		  //hfpage = new HFPage(page);
		  Minibase.BufferManager.unpinPage(pageno, true);
		  Minibase.DiskManager.add_file_entry(name, pageno);
		  if(hfi.top == null){
			  hfi.top = hfpage;
		  }
	  }
  }
 
/**
   * Deletes the heap file from the database, freeing all of its pages.
   */
  public void deleteFile() {
	  
	  /*
	  delete heap file
	  make sure all pages are freed
	  */
	  HeapFileNode curr = hfi.top;
	  RID currRid = curr.firstRecord();
	  while(true){
		  currRid = curr.nextRecord(currRid);
		  if(currRid == null){
			  if(curr.next == null){
				  break;
			  }
			  curr = curr.next;
			  currRid = curr.firstRecord();
		  }
	  }
	  
  }
 
  /**
   * Inserts a new record into the file and returns its RID.
 * @throws SpaceNotAvailableException 
   *
   * @throws IllegalArgumentException if the record is too large
   */
  public RID insertRecord(byte[] record) throws ChainException, SpaceNotAvailableException{
	  if (record.length > global.Page.PAGE_SIZE - HFPage.HEADER_SIZE) {
		  throw new SpaceNotAvailableException();
	  }
	  //Create RID and Tuple and put data from record into them
	  //RID newRID = new RID();
	  //PageId pid = getAvailPage(record.length);//Get PageId of a page large enough to store the record
	  
	  
	  //Tuple t = new Tuple(record, global.Page.PAGE_SIZE - record.length, global.Page.PAGE_SIZE);
	  //System.out.println(global.Page.PAGE_SIZE + " vs " + t.getLength() + " vs " + t.getTupleByteArray().length);
	  //Page p = new Page(t.getTupleByteArray());
	  
	  //PageId pid = Minibase.BufferManager.newPage(p, 1);//Add page to Buffer pool
	  
	  //newRID.pageno = pid;//Associate RID with Page
	  
	  //DOUBT: Do we set slotNo?
	  RID rid = null;
	  HeapFileNode curr  = hfi.top;
	  while(curr != null){
			  rid = curr.insertRecord(record);
			  if(rid == null){
				  if(curr.next == null){
					  PageId pageno = new PageId();
					  Page page = new Page();
					  pageno = Minibase.BufferManager.newPage(page, 1);
					  Minibase.BufferManager.unpinPage(pageno, true);
					  curr.next = new HeapFileNode(page, pageno);
					  curr.next.prev = curr;
					  curr = curr.next;
					  rid = curr.insertRecord(record);
					  if(rid == null){
						  System.out.println("Insufficient space");
					  }
					  return rid;
				  }
				  else{
					  curr = curr.next;
				  }
			  }
			  else{
				  return rid;
			  }
	  }
	  //Minibase.BufferManager.unpinPage(rid.pageno, true);//Unpin Page
	  
	  //return newRID;
	  return null;
	}
 
  /**
   * Reads a record from the file, given its id.
   *
   * @throws IllegalArgumentException if the rid is invalid
   */
  public byte[] selectRecord(RID rid) {
	  HeapFileNode curr = hfi.top;
	  RID currRid = curr.firstRecord();
	  
	  while(!currRid.equals(rid)){
		  currRid = curr.nextRecord(currRid);
		  if(currRid == null){
			  if(curr.next == null){
				  throw new IllegalArgumentException("RID not found");
			  }
			  curr = curr.next;
			  currRid = curr.firstRecord();
		  }
	  }
	  //read record from the file based on id
	return curr.selectRecord(currRid);
  	}
 
/**
   * Updates the specified record in the heap file.
 * @return 
   *
   * @throws IllegalArgumentException if the rid or new record is invalid
   */
  public boolean updateRecord(RID rid, Tuple newTuple) throws ChainException{
	  HeapFileNode curr = hfi.top;
	  RID currRid = curr.firstRecord();
	  while(!currRid.equals(rid)){
		  if(curr.nextRecord(currRid) == null){
			  if(curr.next == null){
				  throw new InvalidUpdateException();
			  }
			  curr = curr.next;
			  currRid = curr.firstRecord();
		  }
		  else{
			  currRid = curr.nextRecord(currRid);
		  }
	  }
	  if(rid.getLength() != newTuple.getLength()){
		  //throw new InvalidUpdateException();
	  }
	  curr.updateRecord(currRid, newTuple);
	  return true;
	}
 
  /**
   * Deletes the specified record from the heap file.
 * @return 
   *
   * @throws IllegalArgumentException if the rid is invalid
   */
  public boolean deleteRecord(RID rid) {
	  
	  //delete record from heap file
	  HeapFileNode curr = hfi.top;
	  RID currRid = curr.firstRecord();
	  
	  while(!currRid.equals(rid)){
		  currRid = curr.nextRecord(currRid);
		  if(currRid == null){
			  if(curr.next == null){
				  return false;
			  }
			  curr = curr.next;
			  currRid = curr.firstRecord();
		  }
	  }
	  curr.deleteRecord(rid);
	return true;
	}
 
/**
   * Gets the number of records in the file.
   */
  public int getRecCnt() {
	  //get no. of records in file
	  int numRecords = 1;
	  HeapFileNode curr = hfi.top;
	  RID currRid = curr.firstRecord();
	  if(currRid == null){
		  return 0;
	  }
	  while(curr != null){
		  currRid = curr.nextRecord(currRid);
		  if(currRid == null){
			  if(curr.next == null){
				  break;
			  }
			  else{ 
				  curr = curr.next;
				  currRid = curr.firstRecord();
				  if(currRid != null){
					  numRecords++;
				  }
			  }
		  }
		  else{
			  numRecords++;
		  }
	  }
	  return numRecords;
	}
 
  /**
   * Searches the directory for a data page with enough free space to store a
   * record of the given size. If no suitable page is found, this creates a new
   * data page.
   */
  protected PageId getAvailPage(int reclen) {
	  //DOUBT: Probably not needed
	  //search directory for page with enough free space to store record of given size
	  //if no suitable page found
	  //create data page
	return null;}

public HeapScan openScan() {
	// TODO Auto-generated method stub
	HeapScan hs = new HeapScan(hfi.top);
	return hs;
}

public Tuple getRecord(RID rid) {
	// TODO Auto-generated method stub
	HeapFileNode curr = hfi.top;
	RID currRid = curr.firstRecord();
	  
	while(!currRid.equals(rid)){
		  currRid = curr.nextRecord(currRid);
		  if(currRid == null){
			  if(curr.next == null){
				  break;
			  }
			  curr = curr.next;
			  currRid = curr.firstRecord();
		  }
	  }
	  //read record from the file based on id
	  
	byte[] data = curr.selectRecord(currRid);
	Tuple t = new Tuple(data);
	//DOUBT: is offset and length needed?
	//offset = hfpage.
	//length =
	return t;
}
 
}
