import bpy
import os



os.chdir(r"D:\K\Downloads\kenney_3droadpack_updated\zTMP\TMP@\o")



for material in bpy.data.materials:
    material.user_clear()
    bpy.data.materials.remove(material)
for mesh in bpy.data.meshes:
    mesh.user_clear()
    bpy.data.meshes.remove(mesh)
for obj in bpy.data.objects:
    obj.user_clear()
    bpy.data.objects.remove(obj)



x=0
bx=0
z=0
bz=0
c=""
for fp in [x for x in os.listdir("./") if x.endswith(".obj")]:
    if (fp.split("-")[0]!=c):
        if (c!=""):
            z+=7
            x=0
            bx=0
            bz=0
        c=fp.split("-")[0]
    bpy.ops.import_scene.obj(filepath=fp)
    bpy.data.objects[fp.split(".")[0]].location=(x,z+1,0)
    x+=2
    bx+=1
    if (bx%5==0):
        x+=2
    if (bx>=25):
        x=0
        bx=0
        z+=2
        bz+=1
        if (bz%5==0):
            z+=3