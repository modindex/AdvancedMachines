package jaminv.advancedmachines.util.dialog.control;

public interface IElementStateObserver<T> {
	public void onStateChanged(IDialogElement element, T state);
}
