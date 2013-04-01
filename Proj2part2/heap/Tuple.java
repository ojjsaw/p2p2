package heap;

import global.GlobalConst;

public class Tuple implements GlobalConst {
	public static final int max_size = PAGE_SIZE;
	public byte[] data; // store the data
	private int tuple_offset;
	private int tuple_length;
	private short fldCnt;

	public Tuple()
	{ 
		data = new byte[max_size]; tuple_offset = 0; tuple_length = max_size;
	}

	public Tuple(byte[] atupl) 
	{
		data = atupl; tuple_offset = 0; tuple_length = atupl.length; 
	}

	/** * construction function * * @param atuple * @param offset * @param length */
	public Tuple(byte[] atuple, int offset, int length)
	{ 
		data = atuple; tuple_offset = offset; tuple_length = length;
		
		//NOT NEEDED I THINK Fixed this becasue it's only purpose is to be used to pad the byte to a certain size (See HeapFile.Insert())
		/*tuple_offset = offset; 
		tuple_length = length;
		byte[] d = new byte[tuple_length];
		for(int i = 0; i < tuple_length-tuple_offset; i ++){
			d[tuple_offset+i] = atuple[i];
		}
		data = d;*/
	}

	/** * * @param size */ 
	public Tuple(int size) 
	{ 
		data = new byte[size]; tuple_offset = 0; this.tuple_length = size; 
	}

	/** * Copy a tuple to the current tuple position you must make sure the tuple
	 *  * lengths must be equal 
	 *  * * @param fromTuple * the tuple being copied */
	public void tupleCopy(Tuple fromTuple) { data = fromTuple.getTupleByteArray(); tuple_length = fromTuple.getLength(); }
	public byte[] getTupleByteArray() 
	{ 
		return data; 
	}

	public int getLength() 
	{ 
		return tuple_length; 
	}

	public void setData(byte[] data) 
	{ 
		this.data = data; 
	}

	public void setlength(int tuple_length) 
	{ 
		this.tuple_length = tuple_length; 
	}

	public int getTuple_offset() 
	{ 
		return tuple_offset; 
	}

	public void setTuple_offset(int tuple_offset) 
	{
		this.tuple_offset = tuple_offset; 
	}
	
	//Another possible implementation instead of the one that uses the constructor
	/*public byte[] getPaddedTupleByteArray()
	{
		byte[] b = new byte[this.getLength()+this.getTuple_offset()];
		byte[] d = this.getTupleByteArray();
		for(int i = 0; i < d.length; i ++){
			b[this.getTuple_offset()+i] = d[i];
		}
		return b;
	}*/

}
