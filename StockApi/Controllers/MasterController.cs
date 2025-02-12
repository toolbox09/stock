using Microsoft.AspNetCore.Mvc;
using StockApi.Data;
using StockApi.Dto;

namespace StockApi.Controllers
{
    [ApiController]
    [Route("master")]
    public class MasterController : ControllerBase
    {
        private readonly StockContext _context;

        public MasterController(StockContext context) 
        {
            _context = context;
        }

        [HttpGet("file-list")]
        public IActionResult FileList()
        {
            return Ok(_context.GetMasterFileList());
        }

        [HttpGet("match-file-list")]
        public IActionResult MatchFileList() 
        {
            return Ok(_context.GetMatchFileList());
        }

        [HttpGet]
        public IActionResult Index(string fileName)
        {
            return Ok(_context.GetMaster(fileName));
        }

    }
}
