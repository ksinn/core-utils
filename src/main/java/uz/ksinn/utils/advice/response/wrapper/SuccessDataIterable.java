package uz.ksinn.utils.advice.response.wrapper;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SuccessDataIterable<T> {

    private List<T> objects;
    private Pagination pagination;

    public SuccessDataIterable(Iterable<T> objects) {
        this.objects = new ArrayList<>();
        objects.forEach(this.objects::add);

//        if (objects instanceof Page) {
//
//            Page<T> page = (Page<T>) objects;
//            pagination = buildPagination(
//                    page.getNumber(),
//                    page.getSize(),
//                    page.getTotalPages(),
//                    page.getTotalElements());
//
//        } else

        if (objects instanceof Collection) {
            Collection<T> collection = (Collection<T>) objects;
            pagination = buildPagination(
                    0,
                    collection.size(),
                    1,
                    collection.size());
        }
    }

    public List<T> getObjects() {
        return objects;
    }

    public Pagination getPagination() {
        return pagination;
    }

    private Pagination buildPagination(int page, int limit, int totalPages, long totalObjects) {
        return new Pagination(page, limit, totalPages, totalObjects);
    }

    public static class Pagination {

        private int page;
        private int limit;
        private int totalPages;
        private long totalObjects;

        public Pagination(int page, int limit, int totalPages, long totalObjects) {
            this.page = page;
            this.limit = limit;
            this.totalPages = totalPages;
            this.totalObjects = totalObjects;
        }

        public int getPage() {
            return page;
        }

        public int getLimit() {
            return limit;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public long getTotalObjects() {
            return totalObjects;
        }
    }
}
