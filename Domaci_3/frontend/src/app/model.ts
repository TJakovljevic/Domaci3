export interface JwtToken{
  jwt:string;
}

export interface User{
  id: number,
  first_name: string,
  last_name: string,
  email: string,
  password: string,
  permissions: Permission[];
}

export interface UserDto{
  first_name: string,
  last_name: string,
  email: string,
  password: string,
  permissions: Permission[];
}


export interface Permission{
  id: number,
  name: string
}

export interface PermissionDto{
  id: number;
}
export interface PermissionsResponse {
  permissions: Permission[];
}



export interface ErrorMessage {
  id: number;
  messageDescription: string;
  status: string;
  orderEntity?: OrderEntity;
}

interface OrderEntity {
  id: number;
  status: string;
  active: boolean;
  createdAt: string;
  dishes: Dish[];
  createdBy: UserDto;
}
//Bukv user samo bez liste permisija

export interface PaginatedResponse {
  content: ErrorMessage[];
  pageable: any;
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: any;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface Dish{
  id: number;
  name: string;
  description: string;
  price: number;
}

