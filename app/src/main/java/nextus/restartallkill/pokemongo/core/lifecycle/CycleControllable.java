package nextus.restartallkill.pokemongo.core.lifecycle;

public interface CycleControllable {
	public void onStart();
	public void onResume();
	public void onPause();
	public void onStop();
	public void onDestroy();
	public void onActivityResult();
}
