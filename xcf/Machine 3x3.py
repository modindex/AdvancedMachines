import os
from gimpfu import *

def setVisibleByName(img, layername, vis):
    for layer in img.layers:
        if pdb.gimp_drawable_get_name(layer) == layername:
            layer.visible = vis


def python_fu_Machine(img, drawable, path):

    machines = [
        ["instance\\furnace\\inactive", ["Furnace Red", "Furnace Bottom", "Furnace Back", "Furnace Border Gray", "Furnace Border", "Furnace Shadow"]],
        ["instance\\furnace\\active", ["Furnace Red", "Furnace Bottom", "Furnace Back", "Furnace Border Gray", "Furnace Border", "Furnace Shadow", "Furnace Fire"]],
        ["instance\\alloy\\inactive", ["Alloy Red", "Alloy Bottom", "Alloy Back", "Alloy Border Gray", "Alloy Top", "Alloy Shadow", "Alloy Border"]],
        ["instance\\alloy\\active", ["Alloy Red", "Alloy Bottom", "Alloy Back", "Alloy Border Gray", "Alloy Top", "Alloy Shadow", "Alloy Border", "Alloy Fire", "Alloy Metals"]],
        ["instance\\grinder\\inactive", ["Grinder Inactive Placeholder", "Grinder Shadow Top Placeholder", "Grinder Shadow Bottom Placeholder"]],
        ["instance\\grinder\\active", ["Grinder Active Placeholder", "Grinder Shadow Top Placeholder", "Grinder Shadow Bottom Placeholder"]],
        ["instance\\purifier\\inactive", ["Purifier Inactive Placeholder"]],
        ["instance\\purifier\\active", ["Purifier Active Placeholder"]],
    ]

    types = [
        ["basic", ["Background"]],
        ["compressed", ["Compressed"]],
        ["quad", ["Quad"]],
        ["improbable", ["Improbable"]]
    ]

    
    old = [False for x in range(len(img.layers))]
    for i in range(len(img.layers)):
        old[i] = img.layers[i].visible
        img.layers[i].visible = False
        

    gimp.context_push()
    img.undo_group_start()

    for machinedata in machines:
        machinepath = machinedata[0]
        for layername in machinedata[1]:
            setVisibleByName(img, layername, True)
            
        for typedata in types:
            typename = typedata[0]
            for layername in typedata[1]:
                setVisibleByName(img, layername, True)

            if not os.path.exists(path + "\\" + machinepath + "\\" + typename):
                os.makedirs(path + "\\" + machinepath + "\\" + typename)

            for x in range(0, 3):
                for y in range(0, 3):
                
                    new_image = pdb.gimp_image_duplicate(img)
                    layer = pdb.gimp_image_merge_visible_layers(new_image, CLIP_TO_IMAGE)
                    pdb.gimp_layer_resize(layer, 16, 16, -(x * 16), -(y * 16))
                    pdb.gimp_file_save(new_image, layer, path + "\\" + machinepath + "\\" + typename + "\\f3x3p" + str(x) + str(y) + ".png", '?')
                    pdb.gimp_image_delete

            for layername in typedata[1]:
                setVisibleByName(img, layername, False)

        for layername in machinedata[1]:
            setVisibleByName(img, layername, False)

    for i in range(len(old)):
        img.layers[i].visible = old[i]
            
    img.undo_group_end()
    gimp.context_pop()

        
register(
        "python_fu_Machine_3x3",
        "Save multiple machine files",
        "Save multiple machine files",
        "Jamin VanderBerg",
        "Jamin VanderBerg",
        "2019",
        "<Image>/Filters/jaminv/Machine 3x3...",
        "RGB*, GRAY*",
        [
                (PF_STRING, "path", "Path", "C:\\Users\\jamin\\Documents\\Project\\Minecraft\\AdvancedMachines\\src\\main\\resources\\assets\\advancedmachines\\textures\\blocks\\machine\\")
        ],
        [],
        python_fu_Machine)

main()