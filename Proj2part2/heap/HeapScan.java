package heap;

import chainexception.ChainException;
import global.Minibase;
import global.RID;

public class HeapScan {
	
	/**

	* Constructs a file scan by pinning the directoy header page and initializing

	* iterator fields.

	*/
	HeapFileNode hfn;
	HeapFileNode gn;
	RID gnr;
	protected HeapScan(HeapFileNode h) {
		hfn = h;
		gn = h;
		gnr = h.firstRecord();
		Minibase.BufferManager.pinPage(h.pageno, h.page, false);
	}


	protected void finalize() throws Throwable {}

	/**

	* Closes the file scan, releasing any pinned pages.

	*/
	public void close() throws ChainException{}


	/**

	* Returns true if there are more records to scan, false otherwise.

	*/
	public boolean hasNext() {
		RID rid = new RID();
		if(this.getNext(rid) == null){
			return false;
		}
		return true;
	}

	 

	/**

	* Gets the next record in the file scan.

	* @param rid output parameter that identifies the returned record

	* @throws IllegalStateException if the scan has no more elements

	*/
	public Tuple getNext(RID rid) {
		
			  if(gnr == null){
				  if(gn.next == null){
					  Minibase.BufferManager.unpinPage(hfn.pageno, true);
					  return null;
					  //throw new IllegalStateException();
				  }
				  gn = gn.next;
				  gnr = gn.firstRecord();
			  }
		rid = gnr;
		Tuple temp = new Tuple(gn.selectRecord(rid));
		gnr = gn.nextRecord(gnr);
		return temp;
	}
	
}
