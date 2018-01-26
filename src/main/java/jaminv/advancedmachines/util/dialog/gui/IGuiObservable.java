package jaminv.advancedmachines.util.dialog.gui;

import java.util.ArrayList;
import java.util.List;

import jaminv.advancedmachines.objects.items.ItemStackHandlerObservable.IObserver;

public interface IGuiObservable {
	public void addObserver(IGuiObserver obv);
	public void removeObserver(IGuiObserver obv);
}
