//
// Nod - Copyright 2011 Three Rings Design, Inc.

package flashbang.objects;

import playn.core.GroupLayer;
import playn.core.PlayN;
import flashbang.SceneObject;

public class GroupLayerObject extends SceneObject
{
    @Override public GroupLayer layer ()
    {
        return _layer;
    }

    protected final GroupLayer _layer = PlayN.graphics().createGroupLayer();
}
