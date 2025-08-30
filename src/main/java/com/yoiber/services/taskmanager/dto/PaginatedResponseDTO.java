package com.yoiber.services.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta paginada para listas de tareas")
public class PaginatedResponseDTO<T> {

    @Schema(description = "Lista de elementos")
    private List<T> content;

    @Schema(description = "Total de elementos", example = "25")
    private long totalElements;

    @Schema(description = "Total de páginas", example = "3")
    private int totalPages;

    @Schema(description = "Página actual (basado en 0)", example = "0")
    private int currentPage;

    @Schema(description = "Tamaño de página", example = "10")
    private int pageSize;

    @Schema(description = "Es la primera página", example = "true")
    private boolean first;

    @Schema(description = "Es la última página", example = "false")
    private boolean last;

    public PaginatedResponseDTO() {
    }

    public PaginatedResponseDTO(List<T> content, long totalElements, int totalPages,
                                int currentPage, int pageSize, boolean first, boolean last) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.first = first;
        this.last = last;
    }

    // Getters y Setters
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}