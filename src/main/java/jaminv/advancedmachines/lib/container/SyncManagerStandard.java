package jaminv.advancedmachines.lib.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SyncManagerStandard implements SyncManager {
	
	protected List<SyncSubject> subjects = new ArrayList<SyncSubject>();
	protected int[] cachedFields;
	protected int cachedCount = -1;
	
	public SyncManagerStandard addSubject(SyncSubject subject) {
		this.subjects.add(subject);
		cachedFields = null;
		return this;
	}
	
	public int getFieldCount() {
		if (cachedCount != -1) { return cachedCount; }
		cachedCount = 0;
		for (SyncSubject subject : subjects) {
			cachedCount += subject.getFieldCount();
		}
		return cachedCount;
	}

	@Override
	public void detectAndSendChanges(Container container, List<IContainerListener> listeners) {
		boolean sendAll = false;
		if (cachedFields == null) {
			cachedFields = new int[getFieldCount()];
			sendAll = true;	
		}

        for (IContainerListener listener : listeners) {
            
			int subject = 0, first = 0;
			for (int i = 0; i < getFieldCount(); i++) {
				if (i == 3) {
					int a = 0;
				}
				while (i >= first + subjects.get(subject).getFieldCount()) {
					first += subjects.get(subject).getFieldCount(); subject++;
				}
				int value = subjects.get(subject).getField(i - first);
				
				if (sendAll || cachedFields[i] != value) {
					listener.sendWindowProperty(container, i, value);
					cachedFields[i] = value;
				}
			}
        }
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		int subject = 0, first = 0;
		if (id == 4) {
			int a = 0;
		}
		while(id >= first + subjects.get(subject).getFieldCount()) {
			first += subjects.get(subject).getFieldCount(); subject++;
		}
		subjects.get(subject).setField(id - first, data);
	}	
}
