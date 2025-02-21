using Microsoft.AspNetCore.Mvc;
using StockApi.Data;
using StockApi.Dto;
using StockApi.Entities;
using System.Diagnostics.Metrics;

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

        [HttpGet()]
        public IActionResult Index(string projectName, string masterUrl)
        {
            var work = new Work
            {
                Id = NanoidDotNet.Nanoid.Generate(),
                ProjectName = projectName,
                Master = _context.GetMaster(masterUrl),
            };
            return Ok(work);
        }

        [HttpPost("update")]
        public IActionResult Update(UpdateWorkRequest request)
        {
            return Ok(_context.UpdateWork(request.ProjectName, request.FileName, request.Raws));
        }

        [HttpGet("collect")]
        public IActionResult Collect(string projectName)
        {
            return Ok(_context.Collect(projectName));
        }

    }
}
