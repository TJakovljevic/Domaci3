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
  id:number;
}
