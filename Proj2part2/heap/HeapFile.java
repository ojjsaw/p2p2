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

/**
   * If the given name already denotes a file, this opens it; otherwise, this
   * creates a new empty file. A null name produces a temporary heap file which
   * requires no DB entry.
   */
  public HeapFile(String name) {
	  
	  PageId pageno = null;
	  Page page;
	  HFPage hfpage;
	  
	  pageno = Minibase.DiskManager.get_file_entry(name);
	  
	  if(pageno == null) {
		  pageno = new PageId();
		  page = new Page();
		  pageno = Minibase.BufferManager.newPage(page, 1);
		  
		  Minibase.DiskManager.add_file_entry(name, pageno);
		  
		  //not sure if needed
		  hfpage = new HFPage(page);
		  
	  }
	  
	  //TODO: open the file - openDB?  
	  hpid = new PageId();
	  hpid.pid = pageno.pid;
  }
 
/**
   * Deletes the heap file from the database, freeing all of its pages.
   */
  public void deleteFile() {
	  
	  /*
	  delete heap file
	  make sure all pages are freed
	  */
	  
	  //HFPage page = new HFPage();
	  
	  
  }
 
  /**
   * Inserts a new record into the file and returns its RID.
   *
   * @throws IllegalArgumentException if the record is too large
   */
  public RID insertRecord(byte[] record) throws ChainException{
	  
	  /*
	  insert new record in file
	  return rid
		*/	  
	  return null;
	}
 
  /**
   * Reads a record from the file, given its id.
   *
   * @throws IllegalArgumentException if the rid is invalid
   */
  public byte[] selectRecord(RID rid) {
	  
	  //read record from the file based on id
	  
	return null;
  	}
 
/**
   * Updates the specified record in the heap file.
 * @return 
   *
   * @throws IllegalArgumentException if the rid or new record is invalid
   */
  public boolean updateRecord(RID rid, Tuple newTuple) throws ChainException{
	  
	  //update record in heap file
	  
	return false;
	}
 
  /**
   * Deletes the specified record from the heap file.
 * @return 
   *
   * @throws IllegalArgumentException if the rid is invalid
   */
  public boolean deleteRecord(RID rid) {
	  
	  //delete record from heap file
	return false;
	}
 
/**
   * Gets the number of records in the file.
   */
  public int getRecCnt() {
	  
	  //get no. of records in file
	return 0;
	}
 
  /**
   * Searches the directory for a data page with enough free space to store a
   * record of the given size. If no suitable page is found, this creates a new
   * data page.
   */
  protected PageId getAvailPage(int reclen) {
	  
	  //search directory for page with enough free space to store record of given size
	  //if no suitable page found
	  //create data page
	  
	return null;}

public HeapScan openScan() {
	// TODO Auto-generated method stub
	return null;
}

public Tuple getRecord(RID rid) {
	// TODO Auto-generated method stub
	return null;
}
 
}
