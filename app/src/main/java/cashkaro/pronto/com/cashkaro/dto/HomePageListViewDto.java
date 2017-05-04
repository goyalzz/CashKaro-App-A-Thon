package cashkaro.pronto.com.cashkaro.dto;

import java.util.List;

/**
 * Created by Ankush Goyal on 17/04/17.
 */

public class HomePageListViewDto {

    private HomePageListViewType type;

    private List<String> title;

    public HomePageListViewType getType() {
        return type;
    }

    public void setType(HomePageListViewType type) {
        this.type = type;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }
}
