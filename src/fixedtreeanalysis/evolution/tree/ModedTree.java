package fixedtreeanalysis.evolution.tree;

public interface ModedTree {
	enum mode {initial,fixed,topology}
	mode getMode();
}
