import os
from gimpfu import *

def setVisibleByName(img, layername, vis):
    for layer in img.layers:
        if pdb.gimp_drawable_get_name(layer) == layername:
            layer.visible = vis


def python_fu_Machine(img, drawable, path):

    machines = [
        ["expansion", []],
        ["speed", ["Upgrade Frame", "Speed"]],
        ["productivity", ["Upgrade Frame", "Productivity"]],
        ["instance\\furnace\\inactive", ["Furnace Inactive"]],
        ["instance\\furnace\\active", ["Furnace Active"]],
        ["instance\\grinder\\inactive", ["Grinder Inactive", "Grinder Shadow Top", "Grinder Shadow Bottom"]],
        ["instance\\grinder\\active", ["Grinder Active", "Grinder Shadow Top", "Grinder Shadow Bottom"]],
        ["instance\\purifier\\inactive", ["Purifier Inactive"]],
        ["instance\\purifier\\active", ["Purifier Active"]],
        ["instance\\alloy\\inactive", ["Alloy Inactive", "Alloy Shadows"]],
        ["instance\\alloy\\active", ["Alloy Active", "Alloy Shadows"]],
        ["instance\\melter\\inactive", ["Melter Inactive", "Melter Corners"]],
        ["instance\\melter\\active", ["Melter Active", "Melter Corners"]],
        ["instance\\injector\\inactive", ["Injector Inactive", "Injector Shadows"]],
        ["instance\\injector\\active", ["Injector Inactive", "Injector Active", "Injector Shadows"]],
        ["instance\\stabilizer\\inactive", ["Stabilizer Inactive", "Stabilizer Shadows"]],
        ["instance\\stabilizer\\active", ["Stabilizer Inactive", "Stabilizer Active", "Stabilizer Shadows"]],
        ["inventory\\input", ["IO", "Input"]],
        ["inventory\\output", ["IO", "Output"]],
        ["energy", ["IO", "RF Energy"]],
        ["redstone\\inactive", ["Redstone Inactive"]],
        ["redstone\\active", ["Redstone Active", "Redstone Inactive"]]        
    ]

    types = [
        ["basic", "Basic", "Gray"],
        ["compressed", "Compressed", "Dark"],
        ["quad", "Quad", "Dark"],
        ["improbable", "Impossible", "Black"],
    ]

    config = [
        ["all", [ "Border Top", "Border Left", "Border Right", "Border Bottom"]],
        ["base", []]
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
            setVisibleByName(img, typedata[1], True)

            if not os.path.exists(path + "\\" + machinepath + "\\" + typename):
                os.makedirs(path + "\\" + machinepath + "\\" + typename)

            for filedata in config:
                file = filedata[0]
                for layername in filedata[1]:
                    setVisibleByName(img, layername, True)
                    setVisibleByName(img, layername + " " + typedata[2], True)
                
                new_image = pdb.gimp_image_duplicate(img)
                layer = pdb.gimp_image_merge_visible_layers(new_image, CLIP_TO_IMAGE)
                pdb.gimp_file_save(new_image, layer, path + "\\" + machinepath + "\\" + typename + "\\" + file + ".png", '?')
                pdb.gimp_image_delete

                for layername in filedata[1]:
                    setVisibleByName(img, layername, False)
                    setVisibleByName(img, layername + " " + typedata[2], False)

            setVisibleByName(img, typedata[1], False)

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