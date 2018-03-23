package jaminv.advancedmachines.util.dialog;

import jaminv.advancedmachines.util.dialog.container.IContainerUpdate;

public interface IModalObserver {
	public void onDialogClose(IContainerUpdate data);
}
