package sol3675.middleearthindustry.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Method;

public abstract class MeiApi
{
    protected static MeiApi api = null;

    @Nullable
    public static MeiApi instance()
    {
        if(MeiApi.api == null)
        {
            try
            {
                Class clazz = Class.forName("sol3675.middleearthindustry.implementation.MeiApiImplementation");
                Method instanceAccessor = clazz.getMethod("instance");
                MeiApi.api = (MeiApi)instanceAccessor.invoke(null);
            }
            catch (Exception e)
            {
                return null;
            }
        }
        return MeiApi.api;
    }
}
