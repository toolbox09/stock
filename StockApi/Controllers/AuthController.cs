using Microsoft.AspNetCore.Mvc;
using StockApi.Data;


namespace StockApi.Controllers
{
    [ApiController]
    [Route("auth")]
    public class AuthController : ControllerBase
    {
        private readonly StockContext _context;
        public AuthController(StockContext context)
        {
            _context = context;
        }

        [HttpGet("login")]
        public IActionResult Login(string id, string password )
        {
            var auths = _context.ReadAuths();
            var auth = auths.FirstOrDefault(_ => _.Id == id && _.Password == password, null);
            return auth == null ? Ok(null) : Ok( new { auth.Id, auth.Keyword } );
        }
    }


}
