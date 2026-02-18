import userRepo from "@/app/repo/userRepo";
//GET not needed since login is done in backend using POST

export async function POST(request) {
    const {email, password} = await request.json();
    const user = await userRepo.login(email, password);
    if (user) {
        return new Response(JSON.stringify(user), { status: 200 });
    } else {
        return new Response(JSON.stringify({ message: "Invalid credentials" }), { status: 401 });
    }
}
