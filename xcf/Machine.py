import os
from gimpfu import *

def setVisibleByName(img, layername, vis):
    for layer in img.layers:
        if pdb.gimp_drawable_get_name(layer) == layername:
            layer.visible = vis


def python_fu_Machine(img, drawable, path):

    normal = [
        ["basic", ["Basic"], "Gray"],
        ["compressed", ["Compressed"], "Dark"],
        ["quad", ["Quad"], "Dark"],
        ["improbable", ["Impossible"], "Black"],
	    ["basic_top", ["Basic", "Top"], "Gray"],
        ["compressed_top", ["Compressed", "Top"], "Dark"],
        ["quad_top", ["Quad", "Top"], "Dark"],
        ["improbable_top", ["Impossible", "Top"], "Black"],	
    ]
    
    tank = [
        ["basic", ["Basic copy", "Tank Border", "Tank Glass"], "Gray"],
        ["compressed", ["Compressed copy", "Tank Border", "Tank Glass"], "Dark"],
        ["quad", ["Quad copy", "Tank Border", "Tank Glass"], "Dark"],
        ["improbable", ["Impossible copy", "Tank Border", "Tank Glass"], "Black"], 
		["basic_top", ["Basic copy", "Tank Border", "Tank Glass", "Top Tank"], "Gray"],
        ["compressed_top", ["Compressed copy", "Tank Border", "Tank Glass", "Top Tank"], "Dark"],
        ["quad_top", ["Quad copy", "Tank Border", "Tank Glass", "Top Tank"], "Dark"],
        ["improbable_top", ["Impossible copy", "Tank Border", "Tank Glass", "Top Tank"], "Black"],   
    ]

    machines = [
        ["multiply", [], normal],
        ["speed", ["Upgrade Frame", "Speed"], normal],
        ["productivity", ["Upgrade Frame", "Productivity"], normal],
        ["tank", ["Tank Border", "Tank Glass"], tank]
    ]

    all = [ "Border Top", "Border Left", "Border Right", "Border Bottom"]
    
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
            
        if not os.path.exists(path + "\\base\\" + machinepath):
            os.makedirs(path + "\\base\\" + machinepath)

        for typedata in machinedata[2]:
            typename = typedata[0]
            for layername in typedata[1]:
                setVisibleByName(img, layername, True)
                
            new_image = pdb.gimp_image_duplicate(img)
            layer = pdb.gimp_image_merge_visible_layers(new_image, CLIP_TO_IMAGE)
            pdb.gimp_file_save(new_image, layer, path + "\\base\\" + machinepath + "\\" + typename + "_base.png", '?')
            pdb.gimp_image_delete                

            for layername in all:
                setVisibleByName(img, layername, True)
                setVisibleByName(img, layername + " " + typedata[2], True)
            
            new_image = pdb.gimp_image_duplicate(img)
            layer = pdb.gimp_image_merge_visible_layers(new_image, CLIP_TO_IMAGE)
            pdb.gimp_file_save(new_image, layer, path + "\\base\\" + machinepath + "\\" + typename + "_all.png", '?')
            pdb.gimp_image_delete

            for layername in all:
                setVisibleByName(img, layername, False)
                setVisibleByName(img, layername + " " + typedata[2], False)

            for layername in typedata[1]:
                setVisibleByName(img, layername, False)

        for layername in machinedata[1]:
            setVisibleByName(img, layername, False)

    for i in range(len(old)):
        img.layers[i].visible = old[i]
            
    img.undo_group_end()
    gimp.context_pop()

        
register(
        "python_fu_Machine",
        "Save multiple machine files",
        "Save multiple machine files",
        "Jamin VanderBerg",
        "Jamin VanderBerg",
        "2019",
        "<Image>/Filters/jaminv/Machine...",
        "RGB*, GRAY*",
        [
                (PF_STRING, "path", "Path", "C:\\Users\\jamin\\Documents\\Project\\Minecraft\\AdvancedMachines\\src\\main\\resources\\assets\\advancedmachines\\textures\\blocks\\machine\\")
        ],
        [],
        python_fu_Machine)

main()