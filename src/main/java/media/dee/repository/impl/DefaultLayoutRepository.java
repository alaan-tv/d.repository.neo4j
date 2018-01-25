package media.dee.repository.impl;

import media.dee.core.repository.api.ComponentRepository;
import media.dee.core.repository.api.LayoutRepository;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Map;

@Component(immediate = true,service = LayoutRepository.class)
public class DefaultLayoutRepository implements LayoutRepository {

    @Override
    public Map<String, Object> findOne(String id) {
        return null;
    }

    @Reference
    public void setComponentRepository(ComponentRepository componentRepository){
        Map map = componentRepository.findOne("",null);
        System.out.println(map.get("c"));
    }

}
