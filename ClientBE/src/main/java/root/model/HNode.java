package root.model;

public class HNode {

	Character charac;
    Integer frequency;
    HNode left = null, right = null;

    public HNode(Character charac, Integer frequency) {
        this.charac = charac;
        this.frequency = frequency;
    }
    
    public HNode(){};

    public HNode(Character charac, Integer frequency, HNode left, HNode right) {
        this.charac = charac;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

	public Character getCharac() {
		return charac;
	}

	public void setCharac(Character charac) {
		this.charac = charac;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public HNode getLeft() {
		return left;
	}

	public void setLeft(HNode left) {
		this.left = left;
	}

	public HNode getRight() {
		return right;
	}

	public void setRight(HNode right) {
		this.right = right;
	}
	
	public boolean isLeaf() {
		return left==null && right==null;
	}

	@Override
	public String toString() {
		return "HNode [charac=" + charac + ", frequency=" + frequency + ", left=" + left + ", right=" + right + "]";
	}
    
	
}
