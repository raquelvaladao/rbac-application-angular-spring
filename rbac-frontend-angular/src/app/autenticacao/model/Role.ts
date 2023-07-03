export enum Role {
    admin = 'admin',
    pilot = 'piloto',
    team = 'escuderia',
}

export function valueOf(role: string): Role {
    if (role === 'ADMINISTRADOR') {
        console.log(role)
        return Role.admin;
    }
    if (role === 'ESCUDERIA') {
        console.log(role)
        return Role.team;
    }
    console.log(role)
    return Role.pilot;
}