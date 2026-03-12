export type TODO_STATUS = "COMPLETED" | "PENDING";

export interface Todo{
    id:number,
    title:string,
    description:string
    status:TODO_STATUS
}