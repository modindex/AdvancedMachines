package jaminv.advancedmachines.lib.dialog.control;

public interface IElementStateObserver<T> {
	public void onStateChanged(IDialogElement element, T state);
}
