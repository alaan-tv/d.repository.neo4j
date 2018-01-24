
package media.dee.repository.impl;

import media.dee.core.repository.api.ComponentRepository;
import org.osgi.service.component.annotations.Component;

import java.util.Map;

@Component(immediate = true, service = ComponentRepository.class)
public class DefaultComponentRepository implements ComponentRepository {

    @Override
    public Map<String,Object> findOne(String ComponentID, Map context) {
        return null;
    }

}
