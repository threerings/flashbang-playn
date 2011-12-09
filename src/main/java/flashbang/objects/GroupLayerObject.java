//
// Nod - Copyright 2011 Three Rings Design, Inc.

package flashbang.objects;

import playn.core.GroupLayer;
import playn.core.PlayN;
import flashbang.SceneObject;

public class GroupLayerObject extends SceneObject
{
    @Override public final GroupLayer layer ()
    {
        return _root;
    }

    protected final GroupLayer _root = PlayN.graphics().createGroupLayer();
}
