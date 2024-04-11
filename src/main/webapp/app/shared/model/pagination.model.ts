export interface IPage<T> {
  content: T[];
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
}
