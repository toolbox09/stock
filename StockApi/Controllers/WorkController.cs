using Microsoft.AspNetCore.Mvc;
using StockApi.Data;
using StockApi.Dto;

namespace StockApi.Controllers
{
    [ApiController]
    [Route("work")]
    public class WorkController : ControllerBase
    {
        private readonly StockContext _context;

        public WorkController(StockContext context) 
        {
            _context = context;
        }

        [HttpPost("update")]
        public IActionResult Update(UpdateWorkRequest request)
        {
            return Ok(_context.UpdateWork(request.ProjectName, request.FileName, request.Raws));
        }

        [HttpPost("collect")]
        public IActionResult Collect(string projectName)
        {
            return Ok(_context.Collect(projectName));
        }

    }
}
